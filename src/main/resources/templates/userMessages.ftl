<#import "parts/common.ftl" as common>

<@common.page>
    <#if isCurrentUser>
        <#include "parts/messageEdit.ftl" />
    </#if>

    <#include "parts/messageList.ftl" />
</@common.page>