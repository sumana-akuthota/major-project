FROM python:3.8.19
WORKDIR /app
COPY . .
RUN pip install --no-cache-dir -r requirements.txt
EXPOSE 8000
ENTRYPOINT [ "python" ]
CMD [ "manage.py", "runserver", "0.0.0.0:8000"  ]