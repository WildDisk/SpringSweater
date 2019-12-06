<#import "parts/common.ftl" as common>
<#import "parts/login.ftl" as login>

<@common.page>
    Add new user
    <P><B><U>${message!}</U></B></P>
    <@login.login "/registration" />
</@common.page>