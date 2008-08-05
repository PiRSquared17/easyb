<%--
  Created by IntelliJ IDEA.
  User: johnbr
  Date: Jul 24, 2008
  Time: 11:05:54 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Story: Create</title></head>
  <body>

   <g:hasErrors bean="${story}">
       <div class="errorbox">
           <g:renderErrors bean="${story}" as="list" />
       </div>
   </g:hasErrors>

  <h1>Story Creator</h1>
   <g:form action="do_create">
      <div class="formdiv">

         <p><span class="prompt">Title:</span> <input type="text" size="45" maxlength="64" name="title"/></p>
         <p><span class="prompt">Description:</span></p>
         <p><textarea rows="5" cols="62" name="description">${scenario?.description}</textarea></p>
         <p />
         <p class="action-button"><input type="submit" value="Create"/></p>
      </div>
   </g:form>


  </body>
</html>