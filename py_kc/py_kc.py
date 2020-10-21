from datetime import datetime
from kafka import KafkaConsumer
from kafka import TopicPartition

def main():
    brokers = 'timemachine:9094'
    topic = 'sotest'
    partitions = [0,1,2]

    # create a consumer
    consumer = KafkaConsumer(bootstrap_servers=brokers)
    toppars = []
    for partition in partitions:
        toppars.append(TopicPartition(topic, partition))
    consumer.assign(toppars)


    # poll loop
    for msg in consumer:
        print('[{}] msg:{}'.format(datetime.now(), msg))
        for header in msg.headers:
            k, v = header
            print('k:{}, v_hex:{}'.format(k, v.hex()))
        print('-'*30)


if __name__ == '__main__':
    main()
