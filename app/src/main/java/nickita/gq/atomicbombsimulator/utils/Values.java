package nickita.gq.atomicbombsimulator.utils;

import nickita.gq.atomicbombsimulator.R;

/**
 * Created by Nickita on 1/3/17.
 */
public class Values {
    public final static int DROP_BOMB = 0;
    public final static int PREPARE_BOMB = 1;
    public final static int SHOW_RESULT = 2;
    public final static int DROP_ANOTHER = 3;
    public final static int ASK_GPS = 4;

    public final static String WARNING_SNACKBAR_CANNOT_DEFINE_LOCATION = "We couldn't define your location. Please try again!";
    public final static String WARNING_SNACKBAR_SHOULD_DROP_PIN_FIRST = "You should drop a bomb target pin on map first!";
    public final static String DB_BOMED_PLACES_LOC = "bombed_places";
    public final static String MARKER_THIS_PLACE_WAS_BOMBED_BY = "This place was bombed by somebody from: ";
    public final static String WARNING_SNACKBAR_CANNOT_GET_LOCATION_NOW = "Can't get your location now. Updating...";
    public final static String RESULT_INSIDE_LEVEL_1 = "Inside Level 1";
    public final static String RESULT_INSIDE_LEVEL_2 = "Inside Level 2";
    public final static String RESULT_INSIDE_LEVEL_3 = "Inside Level 3";
    public final static String RESULT_SURVIVED = "Survived";
    public final static String WARNING_SNACKBAR_LOCATION_DEFINED = "Location defined";
    public final static String ANALYTICS_LOG_TAG_WARNING = "warning_notification";
    public final static String ANALYTICS_LOG_CANNOT_DEFINE_LOCATION = "cannot_define_location";
    public final static String ANALYTICS_CRITICAL_PARSE_OBJECT_ERROR = "cannot_parse_object";

    public final static String ANALYTICS_LOG_SHOULD_DROP_PIN_FIRST = "should_drop_pin_first";
    public final static String[] DAMAGE_LEVELS = {"Level 1", "Level 2", "Level 3"};
    public final static String[] DAMAGE_DESCRIPTIONS = {"(20 psi or 140 kPa)", "(5 psi or 34 kPa)", "(1 psi or 6.9 kPa)"};
    public final static String BOMB_POWER_SELECTOR_LABEL =" Kilotons";

    public static String ERROR_TITLE = "ERROR";
    public final static String FACEBOOK_URI ="https://www.facebook.com/Atomic-Bomb-Simulator-145473922658420/";

    public final static String FEEDBACK_MESSAGE_TITLE = "I really value your feedback!";
    public final static String FEEDBACK_MESSAGE_BODY = "This app is downloaded more than 700 times per day!" +
            "\nThere were numerous versions of this app with bugs, crashes and various problems.\nIt became possible to improve it only thanks to you and your feedback :)" +
            "\nPlease, if there is any issue or you want some feature - feel free to contact me via feedback on Google Play, Facebook or by visiting " +
            "www.nickita.gq\nI do read every feedback. Seriously.";

    public final static String BUTTON_CONFIRMATION_TEXT = "Awesome!";

    public final static String INSTRUCTIONS_MESSAGE_TITLE = "How to use ABS app:";
    public final static String INSTRUCTIONS_MESSAGE_BODY = "Make sure GPS is enabled.\n\n1. Make a long touch on the map to drop a target.\n2. Configure the kiloton slider to choose the power of bomb." +
            "\n3. Press an orange button to simulate explosion.\n" +
            "\nNOTE: If map doesn't function properly: on some devices, manual Location permission activation is required.\n" +
            "\nThis is new version 1.5!\nNow all bombed places are saved on map!";

    public final static String MAP_FRAGMENT_TAG = "MapFragment";
    public final static String RESULT_FRAGMENT_TAG = "ResultFragment";
    public final static String CONFIGURATION_FRAGMENT_TAG = "BombConfigurationFragment";

    public final static String MENU_TITLES[] = { "Home", "Profile", "Calendar", "Settings" };

    public final static int MENU_ICONS[] = { R.drawable.icon_settings, R.drawable.icon_settings, R.drawable.icon_settings, R.drawable.icon_settings };


}
