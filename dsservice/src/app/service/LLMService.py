from typing import Optional
import os
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_core.pydantic_v1 import  Field
from pydantic.v1 import BaseModel
from langchain_openai import ChatOpenAI
from langchain_mistralai import ChatMistralAI
from app.service.Expense import Expense
from langchain_core.utils.function_calling import convert_to_openai_tool
from dotenv import load_dotenv, dotenv_values

class LLMService:
    def __init__(self):
        load_dotenv()
        self.prompt = ChatPromptTemplate.from_messages(
            [
                (
                    "system",
                    "You are an expert extraction algorithm. "
                    "Only extract relevant information from the text. "
                    "If you do not know the value of an attribute asked to extract, "
                    "return null for the attribute's value.",
                ),
                ("human", "{text}")
            ]
        )
        self.apiKey = os.getenv("OPEN_AI_KEY")
        self.llm = ChatMistralAI(api_key=self.apiKey, model="mistral-large-latest", temperature=0) #returns instance of "ChatMistralAI"
        self.runnable = self.prompt | self.llm.with_structured_output(schema=Expense)

    def runLLM(self, message):
        return self.runnable.invoke({"text":message})