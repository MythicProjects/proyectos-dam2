import { useState } from "react";
import { Pressable, Switch, Text, View } from "react-native";
import { styles } from "@/styles";

// Login screen: the switch simulates valid / invalid credentials
function LoginScreen({ onLogin }: { onLogin: () => void }) {
  const [validCredentials, setValidCredentials] = useState(false);
  const [error, setError] = useState(false);

  const submit = () => {
    if (validCredentials) onLogin();
    else setError(true);
  };

  return (
    <View style={styles.screen}>
      <View style={styles.card}>
        <Text style={styles.title}>Iniciar sesión</Text>

        <View style={styles.row}>
          <Text style={styles.label}>Credenciales válidas</Text>
          <Switch value={validCredentials} onValueChange={setValidCredentials} />
        </View>

        {error && <Text style={styles.error}>Credenciales incorrectas</Text>}

        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={submit}
        >
          <Text style={styles.buttonText}>Entrar</Text>
        </Pressable>
      </View>
    </View>
  );
}

function HomeScreen({ onLogout }: { onLogout: () => void }) {
  return (
    <View style={styles.screen}>
      <View style={styles.card}>
        <Text style={styles.title}>Bienvenido</Text>

        <Pressable
          style={({ pressed }) => [styles.button, pressed && styles.buttonPressed]}
          onPress={onLogout}
        >
          <Text style={styles.buttonText}>Salir</Text>
        </Pressable>
      </View>
    </View>
  );
}

// The parent keeps the state and decides which screen to show
export default function Index() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  return isLoggedIn ? (
    <HomeScreen onLogout={() => setIsLoggedIn(false)} />
  ) : (
    <LoginScreen onLogin={() => setIsLoggedIn(true)} />
  );
}
