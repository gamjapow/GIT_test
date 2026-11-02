package com.example.soap;

import jakarta.xml.ws.Endpoint;

/**
 * 내장 Jetty로 SOAP 서버를 기동합니다.
 * 주소는 -Dsoap.address=... 로 변경할 수 있습니다.
 */
public class ServerMain {

    public static void main(String[] args) throws InterruptedException {
        String address = System.getProperty("soap.address", "http://0.0.0.0:8080/ws/user");

        Endpoint endpoint = Endpoint.publish(address, new UserServiceImpl());
        Runtime.getRuntime().addShutdownHook(new Thread(endpoint::stop));

        System.out.println("SOAP server started: " + address);
        System.out.println("WSDL: " + address.replace("0.0.0.0", "localhost") + "?wsdl");
        Thread.currentThread().join();
    }
}
