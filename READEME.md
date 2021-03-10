A server that provides live data about the tube status in London.

**How to run it**:

1. You will need to get an API key from https://api.tfl.gov.uk/

   Place your API key in an environment variable named _TUBE_API_KEY_


2. Then simply run this command at the root of the project:

   ```sbt run```


Note: If you are on Windows and you see strange logging being produced, then try to run this instead:

```sbt --no-colors run```
