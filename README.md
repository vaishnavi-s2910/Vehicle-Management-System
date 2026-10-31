# Vehicle Management System — Java + JavaFX

## Eclipse setup
1. Install **JDK 21** and **Eclipse IDE for Java Developers**.
2. Extract this ZIP file.
3. Open Eclipse → **File → Import → Maven → Existing Maven Projects**.
4. Browse to the extracted `VehicleManagementSystem` folder.
5. Select `pom.xml` → **Finish**.
6. Wait for Maven dependencies to download. If needed, right-click project → **Maven → Update Project**.
7. Right-click the project → **Run As → Maven build...**
8. In Goals, enter `clean javafx:run` and click **Run**.

If Eclipse reports a JRE error: Window → Preferences → Java → Installed JREs, add/select JDK 21.

## Features
- Dashboard showing total, available and in-use vehicles
- Add, edit, delete and search vehicle records
- CSV file storage in the project folder (`vehicles.csv`)
- Sample vehicles are created automatically on first run

## Project layers
- `model` — Vehicle data class
- `repository` — CSV file reading and writing
- `service` — application logic and validation
- `controller` — JavaFX screen and event handling
