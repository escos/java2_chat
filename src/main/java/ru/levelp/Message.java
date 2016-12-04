package ru.levelp;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "sender")
    private String sender;
    @Column(name = "receiver")
    private String receiver;
    @Column (name = "time")
    private long time;
    @Column(name = "body")
    private String body;

    public Message() {
    }

    Message(String sender,String receiver, long time, String body){
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.time = time;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long receiver){
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}