
public class Utils {
	private static String[] serverList = {"10.0.0.8 4040 img_sandeep.jpg",
	"10.0.0.8 4040 vid_sandeep.wmv"}; 

	public static String[] getListofServers(){
		return serverList;
	}

	public static String getResponseFromCode(int errorCode){
		String response = "Response";
		switch(errorCode){
		case 200:
			response = "200 Success";
			break;
		case 400:
			response = "400 Invalid Commands";
			break;
		case 401:
			response = "401 Username Already Exists";
			break;
		case 402:
			response = "402 Password does not match";
			break;
		case 403:
			response = "403 Invalid Username/Password";
			break;
		case 404:
			response = "404 File not found";
			break;
		case 405:
			response = "405 Invalid Arguments";
			break;
		case 406:
			response ="406 Not logged in";
			break;
		default:
			response = "400 Invalid Commands";
			break;
		}
		return response;

	}
}
