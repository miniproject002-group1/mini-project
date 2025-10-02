//package com.example.productservice.client.fallback;
//
//import com.example.productservice.exception.BadRequestException;
//import com.example.productservice.exception.NotFoundException;
//import feign.Response;
//import feign.codec.ErrorDecoder;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Configuration
//public class FeignErrorDecoder implements ErrorDecoder {
//
//    private final ErrorDecoder defaultDecoder = new Default();
//
//    @Override
//    public Exception decode(String methodKey, Response response) {
//        return switch (response.status()) {
//            case 400 -> new BadRequestException("Bad request when calling " + methodKey);
//            case 404 -> new NotFoundException("Resource not found when calling " + methodKey);
//            case 500 -> new RuntimeException("Internal server error from " + methodKey);
//            default -> defaultDecoder.decode(methodKey, response);
//        };
//    }
//}
//
