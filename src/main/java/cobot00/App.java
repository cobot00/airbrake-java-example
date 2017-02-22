package cobot00;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import airbrake.AirbrakeNotice;
import airbrake.AirbrakeNoticeBuilder;
import airbrake.AirbrakeNotifier;

public class App {

    private static final String AIRBRAKE_API_KEY = System.getenv("AIRBRAKE_API_KEY");
    private static final String ENVIRONMENT = getEnvironment();

    private static String getEnvironment() {
        String environment = System.getenv("ENVIRONMENT");
        if (environment == null || environment.isEmpty()) {
            return "development";
        }
        return environment;
    }

    public static void main(String[] args) {
        Random seed = new Random();
        final int random = seed.nextInt(3);
        System.out.println("random: " + random);

        Map<String, Object> request = new HashMap<>();
        request.put("args.length", args.length);
        request.put("random", random);

        try {
            switch (random) {
            case 0: {
                String value = args[1];
                System.out.println("value: " + value);
                break;
            }
            case 1: {
                Integer.parseInt("not converted");
                break;
            }
            case 2: {
                throw new RuntimeException("Good bye!");
            }
            }

        } catch (Exception e) {
            e.printStackTrace();
            AirbrakeNotice notice = new AirbrakeNoticeBuilder(AIRBRAKE_API_KEY, e, ENVIRONMENT) {
                {
                    setRequest(this.getClass().toString(), "dummy");
                    request(request);
                }
            }.newNotice();

            AirbrakeNotifier notifier = new AirbrakeNotifier();
            notifier.notify(notice);
            System.out.println("Airbrake notified");
        }
    }
}
