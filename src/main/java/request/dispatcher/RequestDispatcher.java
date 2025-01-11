package request.dispatcher;

import annotations.RequestHandler;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import request.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RequestDispatcher {

    private String getEndpointHandlingClassName( Request request ) {

        String resultClassName = null;
        String classNameHandlingNotFound = null;

        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);

        provider.addIncludeFilter(new AnnotationTypeFilter(RequestHandler.class));

        Set<BeanDefinition> beanDefs = provider
                .findCandidateComponents("request.handler");

        for ( BeanDefinition bd : beanDefs ) {

            Map<String, Object> annotAttributeMap = ((AnnotatedBeanDefinition) bd)
                    .getMetadata()
                    .getAnnotationAttributes(RequestHandler.class.getCanonicalName());

            if ("1".equals(annotAttributeMap.get("isNotFoundHandler").toString())) {
                classNameHandlingNotFound = bd.getBeanClassName();
                System.out.println("classNameHandlingNotFound = " + classNameHandlingNotFound);
                break;
                //TODO: handle exception when no class is annotated as Not Found Handler
            }

        }

        for ( BeanDefinition bd : beanDefs ) {

            System.out.println("bd class name " + bd.getBeanClassName());

            Map<String, Object> annotAttributeMap = ((AnnotatedBeanDefinition) bd)
                    .getMetadata()
                    .getAnnotationAttributes(RequestHandler.class.getCanonicalName());

            if (request.getRootOfTheEndpoint().equals(annotAttributeMap.get("path").toString())) {
                resultClassName = bd.getBeanClassName();
                System.out.println("resultClassName = " + resultClassName + " endpoint from the request = " + request.getRootOfTheEndpoint());
                break;
                //TODO : add exception to handle case of multiple classes annotated with the same path
            }

        }

        if ( resultClassName == null ) {
            System.out.println("no handler found, resulting in NOT FOUND");
            resultClassName = classNameHandlingNotFound;
        }

        System.out.println("result class name = " + resultClassName);

        return resultClassName;

    }

    public String invokeRequestHandler ( Request request ) {

        String className = getEndpointHandlingClassName( request );
        Class<?> requestHandlerClass = null;
        String response = null;

        try {
            requestHandlerClass = Class.forName( className );

            //TODO: check if class implements IRequestHandler

            Method responseMethod  = requestHandlerClass.getMethod("response", Request.class );

            response = (String) responseMethod.invoke( requestHandlerClass.getDeclaredConstructor().newInstance(), request );

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            System.out.println( "invokeRequestHandler exception: " + className);
            throw new RuntimeException(e);
        }

        return response;

    }

}
