<#import "parts/common.ftl" as common>
<#include "parts/security.ftl">

<@common.page>
    <div>
        <h5>
            Hello, <#if user??>${name}<#else>guest</#if>!
        </h5>
    </div>
    <div>Thi is just a clone Twitter</div>
</@common.page>