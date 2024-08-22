package com.challenge.gft.specification;

import com.challenge.gft.entities.Customer;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import org.springframework.data.jpa.domain.Specification;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplate {

    @And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "document", spec = LikeIgnoreCase.class),
            @Spec(path = "address", spec = LikeIgnoreCase.class)
    })
    public interface CustomerSpec extends Specification<Customer> {}
}
