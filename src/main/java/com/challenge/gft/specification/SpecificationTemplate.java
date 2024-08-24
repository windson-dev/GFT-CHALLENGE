package com.challenge.gft.specification;

import com.challenge.gft.entities.AccountBank;
import com.challenge.gft.entities.Customer;
import com.challenge.gft.entities.Payment;
import jakarta.persistence.criteria.Join;
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

    @And({
            @Spec(path = "status", spec = LikeIgnoreCase.class)
    })
    public interface AccountBankSpec extends Specification<AccountBank> {}

    @And({})
    public interface PaymentSpec extends Specification<Payment> {}

    public static Specification<Payment> filterPaymentByCustomerName(String customerName) {
        return (root, query, builder) -> {
            Join<Payment, AccountBank> senderAccountJoin = root.join("senderAccount");
            Join<AccountBank, Customer> senderCustomerJoin = senderAccountJoin.join("customer");
            Join<Payment, AccountBank> receiverAccountJoin = root.join("receiverAccount");
            Join<AccountBank, Customer> receiverCustomerJoin = receiverAccountJoin.join("customer");
            return builder.or(
                    builder.like(builder.upper(senderCustomerJoin.get("name")), "%" + customerName.toUpperCase() + "%"),
                    builder.like(builder.upper(receiverCustomerJoin.get("name")), "%" + customerName.toUpperCase() + "%")
            );
        };
    }

}
