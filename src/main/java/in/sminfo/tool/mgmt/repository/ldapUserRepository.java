package in.sminfo.tool.mgmt.repository;

import java.util.List;

import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.SearchScope;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.entity.ldapUser;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.List;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
public class ldapUserRepository {

    private static final Integer THREE_SECONDS = 3000;

    @Autowired
    private LdapTemplate ldapTemplate;
    
    //first 2 functions for us only
    public List<ldapUser> getAllUsers() {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("uid").isPresent();

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }

    public List<ldapUser> getPersonNamesByName(String Name) {

        LdapQuery query = query()
                .searchScope(SearchScope.SUBTREE)
                .timeLimit(THREE_SECONDS)
                .countLimit(10)
                .attributes("cn")
                .base(LdapUtils.emptyLdapName())
                .where("objectclass").is("person")
                .and("cn").like("*"+Name+"*")
                .and("uid").isPresent();

        return ldapTemplate.search(query, new PersonAttributesMapper());
    }
    
    public List<ldapUser> getUsersBycn(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        String filter = "(&(objectclass=person)(sn=" + lastName + "))";
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter, sc, new PersonAttributesMapper());
    }

    public List<ldapUser> getPersonNamesByLastName3(String lastName) {

        SearchControls sc = new SearchControls();
        sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        sc.setTimeLimit(THREE_SECONDS);
        sc.setCountLimit(10);
        sc.setReturningAttributes(new String[]{"cn"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("sn", lastName));

        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), sc, new PersonAttributesMapper());
    }

    /**
     * Custom person attributes mapper, maps the attributes to the person POJO
     */
    private class PersonAttributesMapper implements AttributesMapper<ldapUser> {
        public ldapUser mapFromAttributes(Attributes attrs) throws NamingException {
            ldapUser person = new ldapUser();
            person.setCn((String)attrs.get("cn").get());
           // person.setUid((String)attrs.get("uid").get());
            return person;
        }
    }
}

