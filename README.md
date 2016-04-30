# Flume S3 Sink

Special wrapper around the native [HDFSEventSink](https://github.com/apache/flume/blob/flume-1.6/flume-ng-sinks/flume-hdfs-sink/src/main/java/org/apache/flume/sink/hdfs/HDFSEventSink.java#L61)
to be able to use empty `inUseSuffix`.

## Problem description

HDFSEvenSink was designed to write data into HDFS.
When flume is writing data into HDFS it does make sense to use tmp suffix,
because client has to able to distinguish final data and data "in-progress".

S3 doesn't support "append" operation, so flume follows the next workflow:

1. creates temporary file on the agent machine and writes new events to it
2. when file is ready, flume copies it on s3 with `inUseSuffix` in the end
3. finally, flume renames the file by removing `inUseSuffix`

Renaming of files on s3 is essentially 2 operations: "copy to a new file" and "remove the old one".
I was trying to raise [the question via flume user-list](http://mail-archives.apache.org/mod_mbox/flume-user/201512.mbox/%3CC9D2085A-D78B-41EA-9077-DD23201A5735@gmail.com%3E),
but without success. Flume doesn't allow you to specify empty `inUseSuffix` because of:
https://github.com/apache/flume/blob/flume-1.6/flume-ng-configuration/src/main/java/org/apache/flume/conf/FlumeConfiguration.java#L155

## Development

To build the jar file for the sink (tested with gradle 2.2.1):
```
brew install gradle
gradle build
```

## Usage

To use the sink:
1) Place jar into the plugins directory:
```
mkdir -p $FLUME_HOME/plugins.d/flume-s3-sink/lib
cp build/libs/flume-s3-sink-1.0.jar $FLUME_HOME/plugins.d/flume-s3-sink/lib
```
2) Configure the sink:
```
agent.sinks.my_s3sink.type = org.apache.flume.sink.s3.S3Sink
# Other options are the same as for https://flume.apache.org/FlumeUserGuide.html#hdfs-sink
```
