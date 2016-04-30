package org.apache.flume.sink.s3;

import org.apache.flume.Context;
import org.apache.flume.sink.hdfs.HDFSEventSink;

public class S3Sink extends HDFSEventSink {
    @Override
    public void configure(Context context) {
        // Force HDFSSink to use empty inUseSuffix
        String inUseSuffixField = "hdfs.inUseSuffix";
        if (!context.containsKey(inUseSuffixField))
            context.put(inUseSuffixField, "");

        super.configure(context);
    }
}
