package org.perl6.nqp.io;

import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;

import org.perl6.nqp.runtime.ExceptionHandling;
import org.perl6.nqp.runtime.ThreadContext;

public class NotQuiteSocket implements IIOClosable {
    private Socket sock;

    public NotQuiteSocket(ThreadContext tc) {
        sock = new Socket();
    }

    public void close(ThreadContext tc) {
        try {
            sock.close();
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public void connect(ThreadContext tc, String hostname, long port){
        InetSocketAddress addr = new InetSocketAddress(hostname, (int)port);
        try {
            sock.connect(addr);
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }
}
