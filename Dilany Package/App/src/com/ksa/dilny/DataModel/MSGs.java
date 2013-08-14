package com.ksa.dilny.DataModel;

import android.content.Context;
import android.widget.Toast;

public class MSGs {
	
	public static String ERROR_NO_RESULT="E5001" ;
	public static String MSG_ERROR_NO_RESULT="عفواً , لا توجد نتائج حول موقعك الحالي" ;
	
	public static String ERROR_SITE_FAIL="E5004" ;
	public static String MSG_ERROR_SITE_FAIL="عفواً , الموقع خارج الخدمة" ;
	
	public static String ERROR_USER_NOT_EXIST="E5006";
	//public static String MSG_USER_NOT_EXIST="عفواً , هذا المستخدم غير موجود" ;
	public static String MSG_USER_NOT_EXIST="عفواً , بياناتك غير صحيحه" ;
	
	public static String ERROR_USER_NOT_ACTIVATED="E5007";
	public static String MSG_USER_NOT_ACTIVATED="عفواً , لم تقم بتفعيل حسابك بعد" ;
	
	public static String ERROR_LISTING_NOT_EXIST="E5008";
	public static String MSG_LISTING_NOT_EXIST="عفواً , لم يتم العثور على هذا المكان" ;
	
	public static String ERROR_LISTING_ALREADY_RATED="E5009";
	public static String MSG_LISTING_ALREADY_RATED="عفواً , لقد قمت بالتقييم مسبقاً" ;
	
	public static String ERROR_LINK_ARGS="E5014" ;
	public static String MSG_LINK_ARGS="ERROR ERROR ERROR ARGS" ;
	
	public static String ERROR_USER_ALREADY_EXIST="E5016" ;
	public static String MSG_USER_ALREADY_EXIST="عفواً , هذا المستخدم موجود مسبقاً" ;
	
	public static String ERROR_LOGIN_IS_TOO_LONG="EE5017" ;
	public static String MSG_LOGIN_IS_TOO_LONG="عفواً, اسم المستخدم أقل من ٥ حروف أو  أكثر من ٤٠ حرف" ;
	
	public static String ERROR_PASSWORD_IS_TOO_LONG="5018";
	public static String MSG_PASSWORD_IS_TOO_LONG="عفواً, كلمة المرور أقل من ٥ حروف أو أكثر من ٤٠ حرف" ;
	
	public static String ERROR_MAIL_IS_NOT_VALID="E5019" ;
	public static String MSG_ERROR_MAIL_IS_NOT_VALID="عفواً , البريد الإلكتورني غير صحيح" ;
	

	public static String MSG_DONE="D5005" ;
	
	
	public static String MSG_ERROR_PRICE_IS_NO_VALID="عفواً , الرجاء التأكد من صحه بيانات السعر" ;
	public static String MSG_ERROR_FILED_EMPTY="عفواً , الرجاء التأكد من ملئ كافة الحقول" ;
	
	public static void showMessage(Context c,String Code){
		if(Code.equals(ERROR_NO_RESULT)){
			Toast.makeText(c, MSG_ERROR_NO_RESULT, Toast.LENGTH_SHORT).show();
		}else if(Code.equals(ERROR_SITE_FAIL)){
			Toast.makeText(c, MSG_ERROR_SITE_FAIL, Toast.LENGTH_SHORT).show();
		}else if(Code.equals(ERROR_USER_NOT_EXIST)){
			Toast.makeText(c, MSG_USER_NOT_EXIST, Toast.LENGTH_SHORT).show();
		}else if(Code.equals(ERROR_USER_NOT_ACTIVATED)){
			Toast.makeText(c, MSG_USER_NOT_ACTIVATED, Toast.LENGTH_SHORT).show();
		}else if(Code.equals(ERROR_LISTING_NOT_EXIST)){
			Toast.makeText(c, MSG_LISTING_NOT_EXIST, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(ERROR_LISTING_ALREADY_RATED)){
			Toast.makeText(c, MSG_LISTING_ALREADY_RATED, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(ERROR_LINK_ARGS)){
			Toast.makeText(c, MSG_LINK_ARGS, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(ERROR_USER_ALREADY_EXIST)){
			Toast.makeText(c, MSG_USER_ALREADY_EXIST, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(ERROR_LOGIN_IS_TOO_LONG)){
			Toast.makeText(c, MSG_LOGIN_IS_TOO_LONG, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(MSG_PASSWORD_IS_TOO_LONG)){
			Toast.makeText(c, MSG_PASSWORD_IS_TOO_LONG, Toast.LENGTH_SHORT).show();
		} else if(Code.equals(ERROR_MAIL_IS_NOT_VALID)){
			Toast.makeText(c, MSG_ERROR_MAIL_IS_NOT_VALID, Toast.LENGTH_SHORT).show();
		}   
	}
	
	
}
