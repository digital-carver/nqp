package org.perl6.nqp.io;

import java.net.Socket;
import java.net.ServerSocket;
import java.net.InetSocketAddress;
import java.io.IOException;

import org.perl6.nqp.runtime.ExceptionHandling;
import org.perl6.nqp.runtime.ThreadContext;

public class NotQuiteSocket implements IIOClosable {
    private Object sock; // can be either a Socket or a ServerSocket. Fucking Java.

    public NotQuiteSocket(ThreadContext tc) {
        sock = new Socket();
    }

    public NotQuiteSocket(ThreadContext tc, Socket s) {
        sock = s;
    }

    public void close(ThreadContext tc) {
        try {
            if (sock instanceof ServerSocket) {
                ServerSocket s = (ServerSocket)sock;
                s.close();
            } else {
                Socket s = (Socket)sock;
                s.close();
            }
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public void connect(ThreadContext tc, String hostname, long port) {
        InetSocketAddress addr = new InetSocketAddress(hostname, (int)port);
        try {
            if (sock instanceof ServerSocket) { // turn it into a regular Socket
                sock = new Socket();
            }
            Socket s = (Socket)sock;
            s.connect(addr);
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public void bind(ThreadContext tc, String hostname, long port) {
        try {
            ServerSocket s = new ServerSocket();
            InetSocketAddress addr = new InetSocketAddress(hostname, (int)port);
            s.bind(addr);
            sock = s;
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public void listen(ThreadContext tc) {
        // no-op. ServerSocket doesn't seem to have listen() at all, only accept()
    }

    public NotQuiteSocket accept(ThreadContext tc) {
        try {
            ServerSocket s = (ServerSocket)sock;
            return new NotQuiteSocket(tc, s.accept());
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public long read(ThreadContext tc, byte[] buf) {
        try {
            Socket s = (Socket)sock;
            return (long)s.getInputStream().read(buf);
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }

    public void write(ThreadContext tc, byte[] buf) {
        try {
            Socket s = (Socket)sock;
            s.getOutputStream().write(buf);
        } catch (IOException e) {
            throw ExceptionHandling.dieInternal(tc, e);
        }
    }
}
