package com.shava.business.store.control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonLongDocument;

/**
 * Session Bean implementation class Database
 */
@Singleton
@LocalBean
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class Database {

    /**
     * Default constructor. 
     */
    public Database() {
        // TODO Auto-generated constructor stub
    }
    
    CouchbaseCluster cluster;
    Bucket bucket;
    
    @PostConstruct
    public void init() {
        if (!getBucket().exists("user_sequence")) {
        	long counterInit = 1;
        	bucket.insert(JsonLongDocument.create("user_sequence", counterInit));
        }
        if(!getBucket().exists("schedule_sequence")){
        	long counterInit = 0;
        	bucket.insert(JsonLongDocument.create("schedule_sequence", counterInit));
        }
    }
    
    @PreDestroy
    public void stop() {
        bucket.close();
        cluster.disconnect();
    }

    public CouchbaseCluster getCluster() {
        if (null == cluster) {
//            cluster = CouchbaseCluster.create("192.168.99.100");
//            cluster = CouchbaseCluster.create(System.getenv("COUCHBASE_URI"));
            cluster = CouchbaseCluster.create("nosql.database.com");
        }
        return cluster;
    }
    
    public Bucket getBucket() {
        if (null == bucket) {
            bucket = getCluster().openBucket("default");
        }
        return bucket;
    }

}
