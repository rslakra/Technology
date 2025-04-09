#
# Author: Rohtash Lakra
#
from flask import current_app
from unittest import TestCase
from webapp import WebApp


class WebAppTest(TestCase):

    def setUp(self):
        """The setUp() method of the TestCase class is automatically invoked before each test, so it's an ideal place
        to insert common logic that applies to all the tests in the class"""
        print("+setUp()")
        self.webApp = WebApp()
        self.app = self.webApp.create_app(test_mode=True)
        self.app_context = self.app.app_context()
        self.app_context.push()
        print("-setUp()")
        print()

    def tearDown(self):
        """The tearDown() method of the TestCase class is automatically invoked after each test, so it's an ideal place
        to insert common logic that applies to all the tests in the class"""
        print("+tearDown()")
        self.app_context.pop()
        self.webApp = None
        self.app = None
        self.app_context = None
        print("-tearDown()")
        print()

    def test_webapp(self):
        """Tests the WebApp object"""
        print("+test_webapp()")
        assert self.app is not None
        assert current_app == self.app
        # valid object and expected results
        print("-test_webapp()")
        print()
