
package com.xtravels.aspect;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggingAspect {
    private Logger log= LoggerFactory.getLogger(LoggingAspect.class);


    @Pointcut(value = "execution(* com.xtravels.*.*.*(..))")
    public void allPointCut(){

    }

    @Around("allPointCut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {

        ObjectMapper mapper= new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        String methodName=pjp.getSignature().getName();
        String className =pjp.getTarget().getClass().toString();
        Object[] args=pjp.getArgs() ;
        Object object=pjp.proceed();
        try {
            String argus=mapper.writeValueAsString(args);
            log.info("Method invoked "+ className +" : "+methodName +" Argus :"+argus);
            String response=mapper.writeValueAsString(object);
            log.info(className +" : "+methodName +" Response :"+response);

        }catch (JsonProcessingException e){
          log.info("[JsonProcessingException] "+e.getMessage());
        }

        return  object;
    }

}
