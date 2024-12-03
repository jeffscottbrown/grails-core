# Common 7.0 Upgrade Gotchas - DRAFT -

Experienced while upgrading modules for Grails 7

- h2 2.x is stricter about reserved words
  - https://github.com/grails/gorm-hibernate5/pull/910/commits/c8de45df204966ccc228b46b94beeb2142ae0f59 
- [GROOVY-10621](https://issues.apache.org/jira/browse/GROOVY-10621) 
  - Primitive booleans will no longer generate the form of isProperty & getProperty.  They will only generate isProperty()
- Jar artifacts produced by Grails Plugins will no longer have the suffix `-plain`
  - https://github.com/grails/grails-gradle-plugin/pull/347
- [GROOVY-5169](https://issues.apache.org/jira/browse/GROOVY-5169)  [GROOVY-10449](https://issues.apache.org/jira/browse/GROOVY-10449)
  - Fields with a public modifier were not returned with MetaClassImpl#getProperties() in groovy 3, but are now.

## NOTE: This document is a draft and the explanations are only highlights and will be expanded further prior to release of 7.0.

## New Features 
- [grails-gsp #551](https://github.com/grails/grails-gsp/issues/551) adopts a `formActionSubmit` tag to replace `actionSubmit`.  Dispatching actions via a parameter name on a form submit will be removed in a future version of grails.

### Cool New Features
- You can now @Scaffold Controllers and Services and virtually eliminate any boiler plate code.
- Hello Exterminator, Good by bugs! Lot's of things started working... and working well! For instance, use of controller namespaces now work seemlessly.
- Bootstrap 5.3.3 support. Saffolding and Fields tags now optionally support boostrap classes.
- Priortization of AutoConfiguration over bean overriding.
- Lightweight, Removal of numerous dependencies.
- grails-bom overhaul for keeping depedencies up to date and in sync.
- g:form now automatically provides csrf protection when Spring Security CSRF is enabled.
- Massive decoupling of dependencies and cleanup between modules.  SiteMesh dependencies are no longer compiled into controllers fused between numerous modules. SiteMesh isn't even required to use Grails!
- SiteMesh ahs been upgrade to SiteMesh 3!
- Completely up to date modern stack that has been optimized for easier future transitions.
- GSP can now be used OUTSIDE of Grails! see grails-boot
- Works with Spring Security 6 out of the box. No plugin needed!
- Tested and works with Java [17-23](https://github.com/grails/grails-core/blob/0549617f27aeb5b90b64797fa4147dde40fc9c86/.github/workflows/gradle.yml#L18)
