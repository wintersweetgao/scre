package edu.hfu.scre.service.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


import edu.hfu.scre.dao.security.UserGrantDao;
import edu.hfu.scre.entity.AuthGrant;
import edu.hfu.scre.entity.SysStaff;
import edu.hfu.scre.util.CacheData;
import edu.hfu.scre.util.DesEncrypt;
import edu.hfu.scre.util.FormatUtil;
import edu.hfu.scre.util.RandomNum;

@Service
public class UserGrantService implements UserDetailsService{
	private static final Logger LOG = LoggerFactory.getLogger(UserGrantService.class);
	@Resource
	UserGrantDao userGrantDao;

	@Value("${auth.grantCode}") String grantCode;//本系统的授权代码
	
	public List<AuthGrant> getAllGrant(){
		return userGrantDao.getAllGrant(CacheData.getAccessCode(),grantCode);
	}
	
	public Map<String,Object> userReqAuth(String userCode  ){
		String nonce=RandomNum.createRandomString(24)+userCode;
		nonce=DesEncrypt.getEncString(nonce);
		return userGrantDao.userReqAuth(nonce);
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserDetails loadUserByUsername(String userCode)  {
		if (null!=userCode) {
			
			Map<String,Object> mp=userReqAuth(userCode);
			if (null!=mp) {
				for (Map.Entry<String, Object> entry : mp.entrySet()) {
					LOG.debug("loadUserByUsername key= " + entry.getKey() + " and value= " + entry.getValue());
				}
				String mess=String.valueOf(mp.get("mess"));
				if ("succ".equals(mess)) {
					Map<String,Object> p=(Map<String, Object>) mp.get("user");
					SysStaff user=new SysStaff();
					user.setStaffId(Integer.parseInt(String.valueOf(p.get("staffId"))));
					user.setStaffDepart(String.valueOf(mp.get("depart")));
					user.setStaffParentDepart(String.valueOf(mp.get("parentDepart")));
					user.setPoststr(String.valueOf(p.get("poststr")));
					user.setPostId(String.valueOf(p.get("postId")));
					user.setStaffName(String.valueOf(p.get("staffName")));
					
					user.setDepartId(Integer.parseInt(String.valueOf(mp.get("departId"))));
					user.setParentDepartId(Integer.parseInt(String.valueOf(mp.get("parentDepartID"))));
					
					user.setUserCode(userCode);
					user.setStaffTitle(String.valueOf(p.get("staffTitle")));
					user.setUserPass(String.valueOf(p.get("userPass")));
					user.setStaffSex(String.valueOf(p.get("staffSex")));
					user.setStaffPhone(String.valueOf(p.get("staffPhone")));
					user.setStaffAddr(String.valueOf(p.get("staffAddr")));
					user.setStaffEducation(String.valueOf(p.get("staffEducation")));
					user.setStaffNational(String.valueOf(p.get("staffNational")));
					user.setEntryDate(FormatUtil.strToDate(String.valueOf(p.get("entryDate")), "yyyy-MM-dd"));
					user.setStaffBirthDay(FormatUtil.strToDate(String.valueOf(p.get("staffBirthDay")), "yyyy-MM-dd"));
					user.setAccessCode(DesEncrypt.getEncString(userCode+"@"+String.valueOf(mp.get("accessCode"))));
					
					List<Map<String,Object>> userGrants_ls=(List<Map<String,Object>>)mp.get("grants");
					List<AuthGrant> userGrants=new ArrayList<>();
					for(Map<String,Object> gt:userGrants_ls) {
						AuthGrant ag=new AuthGrant();
						ag.setGrantCode(String.valueOf(gt.get("grantCode")));
						ag.setGrantName(String.valueOf(gt.get("grantName")));
						ag.setParentGrantCode(String.valueOf(gt.get("parentGrantCode")));
						userGrants.add(ag);
					}
					List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
					boolean permit=false;
					for(AuthGrant grant:userGrants) {
						LOG.debug("grant:"+grant.getGrantCode()+",parentCode:"+grant.getParentGrantCode());
						if ((null==grant.getParentGrantCode()||
								"null".equals(grant.getParentGrantCode())||
								"".equals(grant.getParentGrantCode()))&&grant.getGrantCode().equals(grantCode)) {
							permit=true;
						}
						GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+grant.getGrantCode());
		                grantedAuthorities.add(grantedAuthority);
					}
					if (permit) {
						UserDetails usd=new MyUserDetails(userCode,user.getUserPass(), grantedAuthorities,user);
						return usd;
					}else {
						throw new RuntimeException("用户"+userCode+",无权访问");
					}
				}else {
					throw new RuntimeException(mess);
				}
			}else {
				throw new RuntimeException("username: " + userCode + " 不存在..");
			}
		}else {
			throw new RuntimeException("非法登入");
		}
		
	}
	
	
}
