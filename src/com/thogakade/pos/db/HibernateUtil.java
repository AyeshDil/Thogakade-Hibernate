package com.thogakade.pos.db;

import com.sun.org.apache.xpath.internal.operations.Or;
import com.thogakade.pos.entity.Customer;
import com.thogakade.pos.entity.Item;
import com.thogakade.pos.entity.Order;
import com.thogakade.pos.entity.OrderDetails;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

    private static SessionFactory sessionFactory = buildSessionFactory();
    private HibernateUtil(){}

    private static SessionFactory buildSessionFactory(){

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure("./hibernate.cfg.xml")
                .build();

        Metadata metadata = new MetadataSources(standardRegistry)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Item.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrderDetails.class)
                .getMetadataBuilder()
                .applyImplicitNamingStrategy(ImplicitNamingStrategyJpaCompliantImpl.INSTANCE)
                .build();

        return metadata.getSessionFactoryBuilder()
                .build();

    }
    public static Session createSession(){
        return sessionFactory.openSession();
    }
}
