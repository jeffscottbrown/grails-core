package org.codehaus.groovy.grails.web.mapping

import grails.web.CamelCaseUrlConverter
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification

/**
 * More tests for {@link LinkGenerator }. See Also LinkGeneratorSpec.
 * 
 * These test focus on testing integration with the URL mappings to ensure they are respected.
 */
class LinkGeneratorWithUrlMappingsSpec extends Specification{

    def baseUrl = "http://myserver.com/foo"
    def context = null
    def path = "welcome"
    def action = [controller:'home', action:'index']

    def mappings = {
        "/${this.path}"(this.action)
    }

    def link = null

    protected getGenerator() {
        def generator = new DefaultLinkGenerator(baseUrl, context)
        def evaluator = new DefaultUrlMappingEvaluator(new MockServletContext())
        generator.urlMappingsHolder = new DefaultUrlMappingsHolder(evaluator.evaluateMappings(mappings ?: {}))
        generator.grailsUrlConverter = new CamelCaseUrlConverter()
        generator
    }

    protected getUri() {
        generator.link(link)
    }

    void "link is prefixed by the deployment context, and use path specified in the mapping"() {
        given:
            context = "/bar"

        when:
            link = action

        then:
            uri == "$context/$path"
    }

    void "absolute links are prefixed by the base url, don't contain the deployment context, and use path specified in the mapping"() {
        given:
            context = "/bar"

        when:
            link = action + [absolute: true]

        then:
            uri == "$baseUrl/$path"
    }

    void "absolute links are generated when a relative link is asked for, but the deployment context is not known or set"() {
        given:
            context = null

        when:
            link = action

        then:
            uri == "$baseUrl/$path"
    }
}
