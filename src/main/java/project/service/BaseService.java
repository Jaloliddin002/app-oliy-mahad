package project.service;


import org.springframework.stereotype.Component;
import project.dto.response.RestAPIResponse;

@Component
public interface BaseService<D,I,P> {

    RestAPIResponse create(D d);
    RestAPIResponse getList (P p);
    RestAPIResponse get (I id);
    RestAPIResponse edit (I id, D d);
    RestAPIResponse delete (I id);

}
