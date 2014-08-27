package com.smartlibrary.gcm;

import android.content.Context;


public class PreferenceUtil extends BasePreferenceUtil
{
  private static PreferenceUtil _instance = null;
  
  private static final String PROPERTY_REG_ID = "APA91bGUTjGLtG36p0lZXcOnCX9MWkX-FkLaDjBbiCC6EK9Suz2SvZIvhO6xNxF-zuKStdHscDEXfH2OCJpbwvXasC_eW29hIUdZOzL0vgvfCk6CV03ewTeNTWuZrnxZjQ9zrVqTpHPyJJ9uvEkzjRqRFB40eC2hGGoAuq7-2_C-5vpKu5vrRw4";
  private static final String PROPERTY_APP_VERSION = "1.9";
  
  
  public static synchronized PreferenceUtil instance(Context $context)
  {
    if (_instance == null)
      _instance = new PreferenceUtil($context);
    return _instance;
  }
  
  
  protected PreferenceUtil(Context $context)
  {
    super($context);
  }
  
  
  public void putRedId(String $regId)
  {
    put(PROPERTY_REG_ID, $regId);
  }
  
  
  public String regId()
  {
    return get(PROPERTY_REG_ID);
  }
  
  
  public void putAppVersion(int $appVersion)
  {
    put(PROPERTY_APP_VERSION, $appVersion);
  }
  
  
  public int appVersion()
  {
    return get(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
  }
}
