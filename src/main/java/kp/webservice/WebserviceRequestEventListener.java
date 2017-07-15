package kp.webservice;

import org.glassfish.jersey.server.ContainerResponse;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.Timer;

public class WebserviceRequestEventListener implements RequestEventListener {
    private final static Logger logger = LoggerFactory.getLogger(WebserviceRequestEventListener.class);
    
    private final int requestNumber;
    private long startTime;
    
    private final Timer connectionTimer;
    private Counter activeRequests;
    private Meter[] responses;
    private Timer.Context context = null;
    
    public WebserviceRequestEventListener(int requestNumber, Counter activeRequests, Timer connectionTimer, Meter[] responses) {
        this.requestNumber = requestNumber;
        this.activeRequests = activeRequests;
        this.activeRequests.inc();
        this.connectionTimer = connectionTimer;
        this.responses = responses;
        
        startTime = System.currentTimeMillis();
        context = this.connectionTimer.time();
    }

    @Override
    public void onEvent(RequestEvent event) {
        switch (event.getType()) {
            case RESOURCE_METHOD_START:
                ExtendedUriInfo uriInfo = event.getUriInfo();
                logger.trace("Processing request {} {}", uriInfo.getMatchedResourceMethod().getHttpMethod(), uriInfo.getRequestUri());
                logger.trace("Starting request #{}.", requestNumber);
                break;
            case FINISHED:
                context.stop();
                logger.trace("Request #{} finished. Processing time: {} ms.", requestNumber, (System.currentTimeMillis() - startTime));
                updateResponses(event.getContainerResponse(), startTime);
                break;
            case EXCEPTION_MAPPER_FOUND:
                break;
            case EXCEPTION_MAPPING_FINISHED:
                break;
            case LOCATOR_MATCHED:
                break;
            case MATCHING_START:
                break;
            case ON_EXCEPTION:
                break;
            case REQUEST_FILTERED:
                break;
            case REQUEST_MATCHED:
                break;
            case RESOURCE_METHOD_FINISHED:
                break;
            case RESP_FILTERS_FINISHED:
                break;
            case RESP_FILTERS_START:
                break;
            case START:
                break;
            case SUBRESOURCE_LOCATED:
                break;
            default:
                break;
        }
    }
    
    private void updateResponses(ContainerResponse containerResponse, long start) {
        if(containerResponse != null) {
            final int responseStatus = containerResponse.getStatus() / 100;
            if (responseStatus >= 1 && responseStatus <= 5) {
                responses[responseStatus - 1].mark();
            }
        }
        activeRequests.dec();
    }
}