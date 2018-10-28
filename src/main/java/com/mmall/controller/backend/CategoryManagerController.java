package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "/add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession httpSession, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //判断是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //是管理员，增加我们处理分类的逻辑
            return iCategoryService.addCategory(categoryName,parentId);

        }else
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    @RequestMapping(value = "/set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession httpSession,Integer categoryId, String categoryName){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //判断是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //更新categoryName
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }
    @RequestMapping(value = "/get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession httpSession, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //判断是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //查询categoryId的子节点信息，并且不递归
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }else
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

    @RequestMapping(value = "/get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession httpSession, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登陆");
        }
        //判断是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
            //0->10->100
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }else
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
    }

}
