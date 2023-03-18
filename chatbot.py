import openai

openai.api_key = "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"

prompt = input("Your prompt: ")

response = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=[
            {"role": "system", "content": "You are a chatbot"},
            {"role": "user", "content": prompt},
        ]
)

result = ''
for choice in response.choices:
    result += choice.message.content

print(result)