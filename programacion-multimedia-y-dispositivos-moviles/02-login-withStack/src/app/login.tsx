import { useState } from "react";
import { Pressable, Switch, Text, View } from "react-native";
import { useAuth } from "@/auth";
import { styles } from "@/styles";

export default function LoginScreen() {
  const { setIsLoggedIn } = useAuth();
  const [validCredentials, setValidCredentials] = useState(false);
  const [error, setError] = useState(false);

  // No navigation code: changing isLoggedIn is enough,
  // the layout unlocks "index" and moves there
  const submit = () => {
    if (validCredentials) setIsLoggedIn(true);
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
