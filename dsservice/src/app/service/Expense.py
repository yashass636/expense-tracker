from typing import Optional
from langchain_mistralai import ChatMistralAI
from langchain_core.pydantic_v1 import Field
from pydantic.v1 import BaseModel

class Expense(BaseModel):
        """Information about a transaction made on any Card"""
        amount: str = Field("0.0", title="expense", description="Expense made on the transaction")               #description is for LLm to understand 
        merchant: str = Field("UnKnown", title="merchant", description="Marchant name whom the transaction has been made")
        currency: str = Field("INR", title="currency", description="currency of the transaction")

        def serialize(self):
                
                return {
                        "amount": self.amount,
                        "merchant": self.merchant,
                        "currency":self.currency
                }