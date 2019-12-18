<#import "parts/common.ftl" as common>
<@common.page>
    List of users
    <table>
        <thead>
        <th>Name</th>
        <th>Role</th>
        <th>Command</th>
        </thead>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a href="/user/${user.id}">edit</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@common.page>