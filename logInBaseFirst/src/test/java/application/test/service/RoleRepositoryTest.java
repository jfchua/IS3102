package application.test.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import application.domain.Role;
import application.repository.RoleRepository;
import application.test.AbstractTest;

@Transactional
public class RoleRepositoryTest extends AbstractTest {


    @Autowired
    private RoleRepository repository;

    @Test
    public void findAllRolesTest() {
    	List<Role> roles = repository.findAll();
    	
    	Assert.assertNotNull("Getting roles should not be null as already inserted via sql", roles);
		Assert.assertEquals("expected no. of roles is 8", 8, roles.size());

    }

}