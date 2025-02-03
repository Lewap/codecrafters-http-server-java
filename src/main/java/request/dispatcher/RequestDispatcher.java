package request.dispatcher;

import annotations.RequestHandler;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import request.Request;
import request.Response;

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
                break;
                //TODO: handle exception when no class is annotated as Not Found Handler
            }

        }

        for ( BeanDefinition bd : beanDefs ) {

            Map<String, Object> annotAttributeMap = ((AnnotatedBeanDefinition) bd)
                    .getMetadata()
                    .getAnnotationAttributes(RequestHandler.class.getCanonicalName());

            if (request.getRootOfTheEndpoint().equals(annotAttributeMap.get("path").toString())) {
                resultClassName = bd.getBeanClassName();
                break;
                //TODO : add exception to handle case of multiple classes annotated with the same path
            }

        }

        if ( resultClassName == null ) {
            resultClassName = classNameHandlingNotFound;
        }

        return resultClassName;

    }

    public String invokeRequestHandler (Request request ) {

        String className = getEndpointHandlingClassName( request );
        Class<?> requestHandlerClass = null;
        Response response = null;

        try {
            requestHandlerClass = Class.forName( className );

            //TODO: check if class implements IRequestHandler

            Method responseMethod = null;

            if ("GET".equals(request.getVERB()))
                responseMethod = requestHandlerClass.getMethod("getResponse", Request.class );

            if ("POST".equals(request.getVERB()))
                responseMethod = requestHandlerClass.getMethod("postResponse", Request.class );

            response = (Response) responseMethod.invoke( requestHandlerClass.getDeclaredConstructor().newInstance(), request );

        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }

        return response.toString();

    }

}
