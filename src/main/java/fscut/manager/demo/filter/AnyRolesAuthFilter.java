package fscut.manager.demo.filter;

import fscut.manager.demo.service.serviceimpl.UserService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AnyRolesAuthFilter extends AuthorizationFilter {


    private UserService userService;

    public AnyRolesAuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response){

		request.setAttribute("anyRolesAuthFilter.FILTERED", true);

	}

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
    	Boolean afterFiltered = (Boolean)(servletRequest.getAttribute("anyRolesAuthFilter.FILTERED"));
        if( BooleanUtils.isTrue(afterFiltered))
        	return true;

        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) mappedValue;

        for (String role: roles
             ) {
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;

//        if(request.getMethod().equals("POST")){
//            String json = new RequestWrapper(request).getBodyString();
//            System.out.println(json);
//            JSONObject jsonObject = JSON.parseObject(json);
//            String productIdStr = jsonObject.getJSONObject("storyUPK").getString("productId");
//            Integer productId = Integer.valueOf(productIdStr);
//            return auth(1, userDto.getUserId(), roles, subject);
//        }
//
//
//        if(request.getMethod().equals("GET")){
//            String str = servletRequest.getParameter("productId");
//            Integer productId = Integer.valueOf(str);
//            return auth(productId, userDto.getUserId(), roles, subject);
//        }

    }

    private boolean auth(Integer productId, Integer userId, String[] roles, Subject subject){
        if(userService.isUserAllowed(productId, userId)){
            for(String role : roles){
                if(subject.hasRole(role)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED);
        return false;
    }

}
