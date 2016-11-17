package grails.test.mixin

import grails.artefact.Artefact
import grails.persistence.Entity
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.springframework.web.servlet.support.RequestContextUtils
import spock.lang.Specification

class ControllerTestForSpec extends Specification implements ControllerUnitTest<SimpleController>, DataTest {

    void setupSpec() {
        mockDomain Simple
    }

    void 'test index'() {
        when:
        controller.index()

        then:
        response.text == 'Hello'
    }

    void 'test total'() {
        when:
        controller.total()

        then:
        response.text == "Total = 0"
    }

    void 'test locale resolver'() {
        when:
        def localeResolver = applicationContext.localeResolver
        request.addPreferredLocale(Locale.FRANCE)

        then:
        localeResolver.resolveLocale(request) == Locale.FRANCE
    }
    
    void 'test locale resolver attribute'() {
        expect:
        RequestContextUtils.getLocaleResolver(request) == applicationContext.localeResolver
    }

}
@Artefact('Controller')
class SimpleController {
    def index = {
        render "Hello"
    }

    def total = {
        render "Total = ${Simple.count()}"
    }
}
@Entity
class Simple {
    Long id
    Long version
    String name
}
