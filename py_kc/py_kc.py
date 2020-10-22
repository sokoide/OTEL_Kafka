from datetime import datetime
from kafka import KafkaConsumer
from kafka import TopicPartition

from opentelemetry import propagators, trace
from opentelemetry.trace import (
        Span,
        DefaultSpan,
        SpanContext
        )
from opentelemetry.exporter import jaeger
from opentelemetry.sdk.trace import (
        TracerProvider,
        sampling,
        SpanContext
        )

from opentelemetry.sdk.trace.export import (
        BatchExportSpanProcessor,
        ConsoleSpanExporter,
        )
from opentelemetry import context as context_api
from opentelemetry import trace as trace_api


import time
import random

global tracer

def otel_init():
    global tracer
    jaeger_exporter = jaeger.JaegerSpanExporter(service_name='py_kc', agent_host_name='timemachine',agent_port=6831)
    trace.set_tracer_provider(TracerProvider(sampler=sampling.ALWAYS_ON))
    span_processor = BatchExportSpanProcessor(jaeger_exporter)
    trace.get_tracer_provider().add_span_processor(span_processor)
    tracer = trace.get_tracer(__name__)

def main():
    otel_init()

    brokers = 'timemachine:9094'
    topic = 'sotest'
    partitions = [0,1,2]

    # create a consumer
    consumer = KafkaConsumer(bootstrap_servers=brokers)
    toppars = []
    for partition in partitions:
        toppars.append(TopicPartition(topic, partition))
    consumer.assign(toppars)


    ids_generator = trace.get_tracer_provider().ids_generator
    # poll loop
    for msg in consumer:
        # print
        print('[{}] msg:{}'.format(datetime.now(), msg))
        tid = None
        sid = None
        for header in msg.headers:
            k, v = header
            print('k:{}, v_hex:{}'.format(k, v.hex()))
            if k=='traceparent':
                # traceparent is a byte array like this
                # b'00-0f900a88ec248149f39125cecd714909-d3f0ef64d9020f01-01'
                tid = int(v[3:35], 16)
                sid = int(v[36:52], 16)
                print('tid:{}, sid:{}'.format(tid, sid))

        parent_span_context = SpanContext(trace_id=tid, span_id=sid, trace_flags=1, trace_state=0,is_remote=True)
        span = DefaultSpan(context=parent_span_context)
        context = {'current-span': span }
        with tracer.start_as_current_span("py_kc", context=context, kind=trace_api.SpanKind.CONSUMER) as s:
            print('context:{}'.format(context))
            print('span:{}'.format(s))

            # dummy sleep
            time.sleep(0.1)
        print('-'*30)


if __name__ == '__main__':
    main()
