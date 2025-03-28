# 1.2.0

Send a test event which will eventually update the test suite instead of updating the test suite directly.

# 1.1.0

Support the web test type by making a POST request to https://<source>.testing.trevorism.com/test.

# 1.0.0

Update dependencies to latest versions. Add permissions to endpoints.

# 0.9.5

Fix bug when no metadata exists.

# 0.9.4

Add testing metadata functionality. Metadata can disable or mark a test suite as 'shouldFail'.
Disabled test suites will not run when invoked. Test suites marked as 'shouldFail' will run but will not create an error if they fail.

# 0.9.3

Add an error when test runs are in failing state for daily notifications.

# 0.9.2

Move the webhook to avoid conflicts with test invocation.

# 0.9.1

Move to event based updating of test suites.

# 0.9.0

Add a webhook for generic errors. Also updated micronaut and gradle dependencies.

# 0.8.0

Update to latest micronaut and datastore utils. Update calling github service.

# 0.7.1

Improve tests, logging and error handling. Require HTTPS

# 0.7.0

Update to java 17, micronaut

# 0.6.0

Remove concept of test suite details and added details into the test suite object.
Added security to the endpoints.
Schedule a check against github actions after test execution to update the test suite with additional data

# 0.5.0

Have testing invoke github actions to run tests.

# 0.4.0

Move to github actions and update dependencies

# 0.3.0

Removed jenkins specific logic from this API