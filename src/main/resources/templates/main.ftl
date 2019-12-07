<#import "parts/common.ftl" as common>

<@common.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <label>
                    <input type="text" name="filter" class="form-control" value="${filter!}"
                           placeholder="Search by tag">
                </label>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <#include "parts/messageEdit.ftl" />

    <#include "parts/messageList.ftl" />

</@common.page>