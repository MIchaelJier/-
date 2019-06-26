package com.team5101.controller;


import com.team5101.mapper.CompetitorMapper;
import com.team5101.mapper.SignUpMapper;
import com.team5101.mapper.UserMapper;
import com.team5101.pojo.Competitor;
import com.team5101.pojo.ContestInfo;
import com.team5101.pojo.SignUp;
import com.team5101.pojo.User;
import com.team5101.service.ContestInfoService;
import com.team5101.service.SignUpService;
import com.team5101.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;




//报名控制器
@RestController
public class RegController {
    @Autowired
    private SignUpService signUpService;
    @Autowired
    private ContestInfoService contestInfoService;
    @Autowired
    private CompetitorMapper competitorMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SignUpMapper signUpMapper;
    @RequestMapping("/baoming")
    public ModelAndView RegInfo(Model model){
        List<SignUp> signUps=signUpService.findAllSignUpInfo();
        model.addAttribute("signups",signUps);
        return new ModelAndView("baoming");
    }
    @RequestMapping("/baoming.getall")
    public ModelAndView getAllSignInfo(Model model){
        List<SignUp> signUps=signUpService.findAllSignUpInfo();
        model.addAttribute("signups",signUps);
        ModelAndView mv=new ModelAndView("baoming");
        System.out.println(signUps.toString());
        return mv;
    }
    @RequestMapping("/contestInfo")
    public ModelAndView getAlltestInfo(Model model, Competitor competitor,HttpServletRequest request){
        List<ContestInfo> contestInfos=contestInfoService.findAllContestInfo();
        model.addAttribute("contestInfos",contestInfos);
        ModelAndView mv=new ModelAndView("ContestInfo");
        User u= (User) request.getSession().getAttribute("USER");
        List<SignUp> signUps=signUpMapper.findSigUPInfo(u.getU_id());
        Competitor c =userService.findInfo(u.getU_sno());
        model.addAttribute("userInfo",c);
        System.out.println(signUps);

        return mv;
    }
    @RequestMapping("/findSigUPInfo")
    public List<SignUp> findSigUPInfo(Model model, HttpServletRequest request){

        User u= (User) request.getSession().getAttribute("USER");
        List<SignUp> signUps=signUpMapper.findSigUPInfo(u.getU_id());
        Competitor c =userService.findInfo(u.getU_sno());

        System.out.println(signUps);

        return signUps;
    }
    //提交报名信息
    @RequestMapping("/regcontestInfo")
    public String getReg(Model model, Competitor competitor, HttpServletRequest request,SignUp signUp) {
        User u = (User) request.getSession().getAttribute("USER");
        Integer c_id = u.getU_id();
        String contestid = request.getParameter("j_id");
        signUp.setC_id(c_id);

        signUp.setJ_id(Integer.parseInt(contestid));

        if (signUpMapper.findInfo(signUp)>=1) {

            return "已报名，请勿重复报名";
        } else {
            System.out.println(contestid);
            signUpMapper.addOne(signUp);
            userMapper.updateOne(competitor);
            ModelAndView mv = new ModelAndView("ContestInfo");
            return "报名成功！";

        }
    }
}
