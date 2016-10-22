package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.PaymentPolicy;

public interface PaymentPolicyRepository extends JpaRepository<PaymentPolicy, Long>{

}
