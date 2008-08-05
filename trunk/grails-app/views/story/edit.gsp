<%--
  Created by IntelliJ IDEA.
  User: johnbr
  Date: Jul 24, 2008
  Time: 1:01:54 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Story Edit</title></head>
  <body>

  <h3>${story.title}</h3>

  <h4>Scenarios</h4>
  <g:if test="${story?.scenarios?.size() > 0}">
        <g:each var='scenario' in="${story.scenarios}">
           <p><g:link action="edit" controller="scenario" id='${scenario.id}'>${scenario.title}</g:link></p>
        </g:each>
    </g:if>
    <g:if test="${story?.scenarios?.size() == 0}">
       <p class="info">You don't have any scenarios yet.  Why not <g:link action='create' controller='scenario' params='[story_id: story.id]'>create one</g:link>?</p>
    </g:if>
    </body>
</html>