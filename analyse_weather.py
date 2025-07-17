import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, mean_absolute_error

# Load data
df = pd.read_csv("data/sample_weather.csv")
df['date'] = pd.to_datetime(df['date'])

# Fill missing values if any
df.fillna(df.mean(numeric_only=True), inplace=True)

# Feature extraction
df['month'] = df['date'].dt.month

# EDA: Temperature over time
plt.figure(figsize=(10, 5))
sns.lineplot(data=df, x='date', y='temperature')
plt.title("Temperature Over Time")
plt.show()

# EDA: Monthly Rainfall
plt.figure(figsize=(10, 5))
sns.barplot(data=df, x='month', y='rainfall')
plt.title("Monthly Rainfall")
plt.show()

# EDA: Temp vs Humidity
sns.scatterplot(data=df, x='humidity', y='temperature')
plt.title("Temperature vs Humidity")
plt.show()

# Linear Regression Model
X = df[['humidity', 'rainfall']]
y = df['temperature']

model = LinearRegression()
model.fit(X, y)

y_pred = model.predict(X)

# Evaluation
print("MAE:", mean_absolute_error(y, y_pred))
print("MSE:", mean_squared_error(y, y_pred))

# Plot actual vs predicted
plt.figure(figsize=(10, 5))
plt.plot(df['date'], y, label='Actual')
plt.plot(df['date'], y_pred, label='Predicted')
plt.legend()
plt.title("Actual vs Predicted Temperature")
plt.show()
