import re

class MessageUtil:
    def isBankSms(self, message):                                   #indicates if the message is a bank message
        words_to_search = ['spent', 'card', 'Bank', 'debited']
        pattern = r'\b(?:' + '|'.join(re.escape(word) for word in words_to_search) + r')\b'     #using regular expression to detect the message type
        return bool(re.search(pattern, message, flags=re.IGNORECASE))