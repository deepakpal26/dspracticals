package com.metlife.ins.portal.formbean;

import com.metlife.ins.portal.controller.PortalController;
import com.metlife.ins.portal.dataobject.Constant;
import com.metlife.ins.portal.dataobject.Profile;
import com.metlife.ins.portal.dataobject.UserProfile;
import com.metlife.ins.portal.exception.ProcessingException;
import com.metlife.ins.portal.exception.ProfileNotFoundException;
import com.metlife.ins.portal.exception.SecurityServiceException;
import com.metlife.ins.portal.exception.ServiceCallException;
import com.metlife.ins.portal.exception.UserDoesNotExistException;
import com.metlife.ins.portal.util.FormatMethods;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public final class EnterUsernameForm extends PortalBaseForm {
  private String username = null;
  
  private String email = null;
  
  // Removing hardecoded credentials as recommended by Veracode 
  
  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String paramString) {
    this.username = paramString;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String paramString) {
    this.email = paramString;
  }
  
  public void reset(ActionMapping paramActionMapping, HttpServletRequest paramHttpServletRequest) {
    this.username = null;
    this.email = null;
  }
  
  public void syncForm(String paramString1, String paramString2, HttpServletRequest paramHttpServletRequest) throws ProcessingException {
    PortalController portalController = new PortalController();
    FormatMethods formatMethods = new FormatMethods();
    this.username = formatMethods.unEscapeAll(portalController.getSessionData(paramString1, paramString2, "username"));
    this.email = formatMethods.unEscapeAll(portalController.getSessionData(paramString1, paramString2, "email"));
    paramHttpServletRequest.setAttribute("username", this.username);
    paramHttpServletRequest.setAttribute("email", this.email);
  }
  
  public ActionErrors validate(ActionMapping paramActionMapping, HttpServletRequest paramHttpServletRequest) {
    ActionErrors actionErrors = super.validate(paramActionMapping, paramHttpServletRequest);
    try {
      PortalController portalController = null;
      if (portalController == null)
        portalController = new PortalController(); 
      Profile profile = portalController.getProfile(this.username, "");
      UserProfile userProfile = profile.getUserProfile();
      if (null != userProfile && !userProfile.getEMail().equalsIgnoreCase(this.email)) {
        Constant constant = portalController.getConstantByName("FORGETPWD.ERR.NOPROFILE");
        paramHttpServletRequest.setAttribute("PORTAL.FORM.ERR", constant.getValue());
        ActionMessage actionMessage = new ActionMessage("FORGETPWD.ERR.NOPROFILE");
        actionErrors.add("username", actionMessage);
      } 
    } catch (ProfileNotFoundException profileNotFoundException) {
      this.log.warn(this.context, 1010, "EnterUsernameForm.validate()-ProfileNotFoundException ", (Throwable)profileNotFoundException);
    } catch (UserDoesNotExistException userDoesNotExistException) {
      this.log.warn(this.context, 1010, "EnterUsernameForm.validate()-UserDoesNotExistException ", (Throwable)userDoesNotExistException);
    } catch (SecurityServiceException securityServiceException) {
      this.log.warn(this.context, 1010, "EnterUsernameForm.validate()-SecurityServiceException ", (Throwable)securityServiceException);
    } catch (ServiceCallException serviceCallException) {
      this.log.warn(this.context, 1010, "EnterUsernameForm.validate()-ServiceCallException ", (Throwable)serviceCallException);
    } catch (ProcessingException processingException) {
      this.log.warn(this.context, 1010, "EnterUsernameForm.validate()-ProcessingException ", (Throwable)processingException);
    } 
    return actionErrors;
  }
}


/* Location:              C:\portalprod\!\com\metlife\ins\portal\formbean\EnterUsernameForm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */