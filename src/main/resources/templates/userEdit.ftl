<#import "parts/common.ftl" as common>
<@common.page>
    User editor
    <form action="/user" method="post">
        <label>
            <input name="username" type="text" value="${user.username}">
        </label>
        <#list roles as role>
            <div>
                <label>
                    <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>${role}
                </label>
            </div>
        </#list>
        <input name="userId" type="hidden" value="${user.id}">
        <input name="_csrf" type="hidden" value="${_csrf.token}">
        <button type="submit">Save</button>
    </form>
</@common.page>