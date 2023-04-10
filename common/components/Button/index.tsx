// import Loading from 'components/Loading';
import React, { ReactNode } from 'react';
import { Container } from './styles';

interface ButtonProps {
  label: string;
  icon?: ReactNode;
  type?: 'button' | 'submit' | 'reset';
  disabled?: boolean;
  size?: 'small' | 'default';
  variant?: 'colored' | 'outlined' | 'no-outlined';
  theme?: 'primary' | 'secondary' | 'tertiary';
  active?: boolean;
  isLoading?: boolean;
  onClick?: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void;
}

export default function Button({
  label,
  icon,
  type = 'button',
  disabled = false,
  size = 'default',
  variant = 'colored',
  theme = 'primary',
  active = false,
  isLoading = false,
  onClick,
}: ButtonProps) {
  const activeClass = active ? '--active' : '';

  return (
    <Container
      type={type}
      className={[`--${size}`, `--${variant}`, `--${theme}`, activeClass].join(' ')}
      onClick={onClick}
      disabled={isLoading || disabled}
    >
      {isLoading ? (
        <span>Loading</span>
        // <Loading />
      ) : (
        <>
          {icon} {label}
        </>
      )}
    </Container>
  );
}
