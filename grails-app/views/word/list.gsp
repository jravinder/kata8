
<%@ page import="kata8.Word" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'word.label', default: 'Word')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}">Home</a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'word.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="dictionaryWord" title="${message(code: 'word.dictionaryWord.label', default: 'Dictionary Word')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${wordInstanceList}" status="i" var="wordInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${wordInstance.id}">${fieldValue(bean: wordInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: wordInstance, field: "dictionaryWord")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${wordInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
