# SoftwareSecurityAccessControl
## Software Security Second Handout - Access Control and Capabilities

Handout 2
* You will be using the authenticator class and user management system already developed in Handout 1
* You will be designing and coding a capability based access control component.
* You will be required to integrate both components on a toy contact list application

Contact List Application
* The system allows users to manage a list of contacts for other users. Each contact contains name, phone, email, public statement and internal statement.
* Each user manages a profile, with private, internal, and public parts, and a “friends” list, which is a list of other users in the system.
* The private part of profile can only be seen by the owner of the profile, the internal part may be also seen by his friends. A profile may only be changed by the owner.
* The public part of the profile may be seen by anyone.
* There is a special user (the superwoman) that may block any other user in the system. If blocked, a user “disapears” from the system, until unblocked back.
Departamento de Informática FCT UNL (uso reservado © )
Contact List Application
* (advanced +2) each user may define the privacy level of all of its data: private, internal or public.
* (advanced +2) each user may ask for friendship, which if granted would allow it to see the internal info of another user.
* All parts of the application must be subject to appropriate authentication, access control, and information flow policies.
* Define carefully what are the principals, roles, access control policies, and information flow lattices.
* Get back to me for any questions.
* Due date (end of November), grad 0-22.

## Minimal Access Control Manager API
```Java
Capability makeCapability (Owner, Grantee, nonce, Resource, Operation, Time)


boolean Capability checkPermission (Principal, nonce, Capability, Resource, Operation)


CapabilityList getCapabilities (HTTPRequest req)
```
The “nonce” can be included to add extra trust in grantee authentication, depending on the scheme.

### Structure of Capability (simple example)
payload:
* OwnerId
* GranteeId
* ResourceId/Operation pair(s) authorised Timestamp signed capability encoded as a string:
```Java
encbase64(payload)+“.”+encbase64(sign(key,encbase64(payload))
```
Also, capabilities should be sent over encrypted channels.
There are standards to define capabilities (e.g. Jason WT)
 
### Structure of Capability (simple example)
payload:
 * OwnerId
 * GranteeId
 * ResourceId/Operation pair(s) authorised Timestamp signed capability encoded as a string:
```Java
encbase64(payload)+“.”+encbase64(sign(key,encbase64(payload))
```
the key is key private to the resource owner, and used to
sign authorisations. It may be refreshed frequently and / or
derived from the principal authentication key.
 
### Access Controller Class
Responsible by:
* managing resources (ID) and owners (principals) • managing capabilites (a.k.a. “access keys”)
* create new “capabilities” (new “access keys”)
* this involves checking authority of the principal creating the key, in particular his identity and ownership of the resource
* expire “capabilities”
* providing a means for a checking if an authenticated principal can use a resource / operation
* The Access Controller uses the Authenticator module and imports all related concepts and entities
